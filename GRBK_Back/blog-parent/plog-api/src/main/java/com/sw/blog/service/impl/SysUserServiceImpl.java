package com.sw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sw.blog.dao.mapper.SysUserMapper;
import com.sw.blog.dao.pojo.SysUser;

import com.sw.blog.service.LoginService;
import com.sw.blog.service.SysUserService;
import com.sw.blog.vo.ErrorCode;
import com.sw.blog.vo.LoginUserVo;
import com.sw.blog.vo.Result;
import com.sw.blog.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private LoginService loginService;

//    @Autowired
//    private LoginService loginService;

//    @Override
//    public UserVo findUserById(Long id) {
//        SysUser sysUser = sysUserMapper.selectById(id);
//        if (sysUser == null){
//            sysUser = new SysUser();
//            sysUser.setId(1L);
//            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
//            sysUser.setNickname("码神之路");
//        }
//        UserVo userVo  = new UserVo();
//        BeanUtils.copyProperties(sysUser,userVo);
//        userVo.setId(String.valueOf(sysUser.getId()));
//        return userVo;
//    }

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("未知访客");
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        }
        return sysUser;
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("未知访客");
        }
        UserVo userVo=new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }
//
//    @Override
//    public SysUser findUser(String account, String password) {
//        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysUser::getAccount,account);
//        queryWrapper.eq(SysUser::getPassword,password);
//        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
//        queryWrapper.last("limit 1");
//
//        return sysUserMapper.selectOne(queryWrapper);
//    }
//
    @Override
    public Result findUserByToken(String token) {
        /**
         * 1. token合法性校验
         *    是否为空，解析是否成功 redis是否存在
         * 2. 如果校验失败 返回错误
         * 3. 如果成功，返回对应的结果 LoginUserVo
         */
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        //loginUserVo.setId(sysUser.getId());
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());
        return Result.success(loginUserVo);
    }

//    @Override
//    public SysUser findUserByAccount(String account) {
//        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysUser::getAccount,account);
//        queryWrapper.last("limit 1");
//        return this.sysUserMapper.selectOne(queryWrapper);
//    }
//
//    @Override
//    public void save(SysUser sysUser) {
//        //保存用户这 id会自动生成
//        //这个地方 默认生成的id是 分布式id 雪花算法
//        //mybatis-plus
//        this.sysUserMapper.insert(sysUser);
//    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户这 id会自动生成
        //这个地方 默认生成的id是 分布式id 雪花算法
        //mybatis-plus
        this.sysUserMapper.insert(sysUser);
    }


}
