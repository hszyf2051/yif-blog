package com.yif.admin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yif.admin.mapper.PermissionMapper;
import com.yif.admin.model.params.PageParam;
import com.yif.admin.pojo.Permission;
import com.yif.admin.service.PermissionSerivce;
import com.yif.admin.vo.PageResult;
import com.yif.admin.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionSerivce {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public ResultVo listPermission(PageParam pageParam) {
        /**
         * 管理台 表中的所有对象
         * 分页查询
         */
        Page<Permission> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Permission> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getQueryString())) {
            lqw.eq(Permission::getName,pageParam.getQueryString());
        }
        Page<Permission> permissionPage = permissionMapper.selectPage(page,lqw);
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());
        return ResultVo.success(pageResult);
    }

    @Override
    public ResultVo add(Permission permission) {
        this.permissionMapper.insert(permission);
        return ResultVo.success(null);
    }

    @Override
    public ResultVo update(Permission permission) {
        this.permissionMapper.updateById(permission);
        return ResultVo.success(null);
    }

    @Override
    public ResultVo delete(Long id) {
        this.permissionMapper.deleteById(id);
        return ResultVo.success(null);
    }
}
