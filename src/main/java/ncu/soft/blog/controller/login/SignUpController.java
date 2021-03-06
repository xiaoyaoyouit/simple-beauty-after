package ncu.soft.blog.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.zhenzi.sms.ZhenziSmsClient;
import io.swagger.annotations.ApiOperation;
import ncu.soft.blog.entity.Users;
import ncu.soft.blog.entity.UsersInfo;
import ncu.soft.blog.entity.UsersTheme;
import ncu.soft.blog.service.UserService;
import ncu.soft.blog.service.UsersInfoService;
import ncu.soft.blog.service.UsersThemeService;
import ncu.soft.blog.utils.GetString;
import ncu.soft.blog.utils.JsonResult;
import ncu.soft.blog.utils.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author www.xyjz123.xyz
 * @description 用户注册相关api接口
 * @date 2019/8/18 13:51
 */
@RestController
public class SignUpController {

    @Resource
    UserService userService;

    @Resource
    UsersInfoService usersInfoService;

    @Resource
    ValueOperations<String ,Object> valueOperations;

    @Resource
    UsersThemeService usersThemeService;

    private final static String SMS = "smsCaptcha";

    @ApiOperation("发送短信验证码")
    @PostMapping("/phone")
    public JsonResult sendMsg(@RequestParam("phone") String phone) throws Exception{
        System.out.println(phone);
        String code = GetString.getCode();
        System.out.println("code = " + code);
        // 榛子短信的SDK
        // 连接榛子云需要的信息
        ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com", "101348", "ZGZmNjM3MWYtZDVjMS00YWUyLWE4NmUtZDI5NjNmOGRjNTA1");
        String result = client.send(phone, "您的验证码为" + code + "\n" + "如果不是本人操作，请忽略。");
        JSONObject jsonObject = JSONObject.parseObject(result);
        if ((Integer) jsonObject.get("code") != 0){
            return JsonResult.failure(ResultCode.INTERFACE_REQUEST_TIMEOUT);
        }

        //设置验证码1分钟后过期
        valueOperations.set(SMS,code,1, TimeUnit.MINUTES);

        return JsonResult.success();
    }

    @ApiOperation("检查验证码是否正确")
    @PostMapping("/captcha")
    public JsonResult checkVerifyCode(@Valid @RequestParam("key")String key,
            @Valid @RequestParam("code") String code){
        String verifyCode = (String) valueOperations.get(key);
        if (verifyCode == null){
            return JsonResult.failure(ResultCode.CAPTCHA_HAS_EXPIRED);
        }
        if (!StringUtils.equalsIgnoreCase(code,verifyCode)){
            return JsonResult.failure(ResultCode.CAPTCHA_IS_ERROR);
        }
        return JsonResult.success();
    }

    @ApiOperation("账号注册")
    @PostMapping("/register")
    public JsonResult addUser(@Valid @RequestBody Users users){
        Users users1 = userService.save(users);
        UsersInfo usersInfo = new UsersInfo(String.valueOf(users1.getId()),users.getUAccount(),users.getUAccount(),
                "http://cdn.jie12366.xyz/head_boy.png",0,0,0,0,0);
        usersInfoService.save(usersInfo);
        // 初始化默认主题
        UsersTheme usersTheme = new UsersTheme(String.valueOf(users1.getId()), "http://cdn.jie12366.xyz/dog.jpg",
                "http://cdn.jie12366.xyz/sky.png","paraiso-light");
        usersThemeService.save(usersTheme);
        return JsonResult.success();
    }

    @ApiOperation("重置密码")
    @PutMapping("/password")
    public JsonResult resetPwd(@Valid @RequestParam("email") String email,@RequestParam("pwd") String pwd){
        Users users = userService.updatePwd(email,pwd);
        if (users != null){
            return JsonResult.success();
        }else {
            return JsonResult.failure(ResultCode.UPDATE_ERROR);
        }
    }
}
