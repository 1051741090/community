package com.nowcoder.community.b_dao;

import com.nowcoder.community.a_entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Dao是产品经理给的接口，直接粘
//从Dao开始转到MB123路径了，完成再？
@Mapper
@Repository
public interface DiscussPostMapper1 {
    //userid无时是查所有-分页用，userid有时是查一个人帖子后期用
    List<DiscussPost> selectDiscussPosts(@Param("userId")int userId, @Param("offset")int offset, @Param("limit")int limit);

    //这个userid发了几条题解，后期用
    int selectDiscussPostRows(@Param("userId")int userId);
}
