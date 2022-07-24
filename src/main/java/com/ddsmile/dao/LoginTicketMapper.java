package com.ddsmile.dao;

import com.ddsmile.entity.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户登录凭证数据访问层
 * 用来对 login_ticket表 进行增删改查。
 */

//这里的SQL语句没有写mapper对应的xml文件，而是采用了注解的方式
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values (#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id") //将id属性设为自动生成主键
    /**
     *  插入一条登录凭证
     * @param loginTicket
     * @return
     */
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    /**
     *  通过登录凭证ticket查询 用户登录中的数据
     * @param ticket
     * @return
     */
    LoginTicket selectByTicket(String ticket);

    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket} "
    })
    /**
     *  更新状态, 表示登录凭证已经失效
     * @param ticket
     * @param status 状态,0-有效; 1-无效
     * @return
     */
    int updateStatus(String ticket, int status);
}
