package ncu.soft.blog.service;

import ncu.soft.blog.entity.Article;
import org.springframework.data.domain.PageImpl;

/**
 * @author www.xyjz123.xyz
 * @description 文章管理业务层
 * @date 2019/8/25 19:47
 */
public interface ArticlesService {

    /**
     * 保存文章信息
     * @param article Article
     * @return 返回存入的对象
     */
    Article save(Article article,String contentHtml);

    /**
     * 分页查询所有数据
     * @param pageIndex 当前页
     * @param pageSize 每页大小
     * @return PageImpl
     */
    PageImpl<Article> getArticlesByPage(int pageIndex,int pageSize);

    /**
     * 根据用户id分页查询数据
     * @param pageIndex 当前页
     * @param pageSize 每页大小
     * @param uid 用户id
     * @return PageImpl<Article>
     */
    PageImpl<Article> getArticlesByUidByPage(int pageIndex,int pageSize,int uid);

    /**
     * 根据aid获取文章数据
     * @param aid 文章id
     * @return Article
     */
    Article getArticle(int aid);

    /**
     * 阅读量+1
     * @return 更新后的对象
     */
    Article updateReads(int aid);

    /**
     * 获取用户指定标签的文章
     * @param index 当前页
     * @param size 每页大小
     * @param uid 用户id
     * @param tag 文章标签
     * @return PageImpl
     */
    PageImpl<Article> getArticleByTag(int index,int size,int uid,String tag);

    /**
     * 获取用户指定标签的文章
     * @param index 当前页
     * @param size 每页大小
     * @param uid 用户id
     * @param category 文章分类
     * @return PageImpl
     */
    PageImpl<Article> getArticleByCategory(int index,int size,int uid,String category);

    /**
     * 获取用户指定归档的文章
     * @param index 当前页
     * @param size 每页大小
     * @param uid 用户id
     * @param archive 文章归档
     * @return PageImpl
     */
    PageImpl<Article> getArticleByArchive(int index,int size,int uid,String archive);
}