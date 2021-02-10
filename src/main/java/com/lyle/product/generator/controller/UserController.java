package com.lyle.product.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyle.product.generator.constants.CommonConstant;
import com.lyle.product.generator.model.ComResponse;
import com.lyle.product.generator.model.User;
import com.lyle.product.generator.service.UserService;
import com.lyle.product.generator.util.EncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;


    @GetMapping("/login")
    public ComResponse<User> login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response) throws UnsupportedEncodingException {

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email).eq("password", EncryptUtils.encryptMd5(password));
        User user = userService.getOne(wrapper);
        if (null == user) {
            return new ComResponse<User>().noUser();
        }

        //LYLEPLAN 后续将系统的的cookie 验证换为jwt 认证
        Cookie cookie = new Cookie("userId", user.getId().toString());
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        //密码不传给前端
        user.setPassword(null);

        return new ComResponse<User>().success(user, "请求成功");
    }

    @PutMapping("/update")
    public ComResponse<Boolean> update(User user) throws UnsupportedEncodingException {
        user.setPassword(EncryptUtils.encryptMd5(user.getPassword()));
        return new ComResponse<Boolean>().success(this.userService.updateById(user));
    }

    @DeleteMapping("/delete/{userId}")
    public ComResponse<Boolean> delete(@PathVariable("userId") Long userId) {
        return new ComResponse<Boolean>().success(this.userService.removeById(userId));
    }

    @PostMapping("/register")
    public ComResponse<Boolean> register(User user) throws UnsupportedEncodingException {
        User tempUser = userService.getOne(new QueryWrapper<User>().eq("email", user.getEmail()));

        if (null != tempUser) {
            return new ComResponse<Boolean>().failed(false, "用户已存在");
        }

        user.setPassword(EncryptUtils.encryptMd5(user.getPassword()));

        if (StringUtils.isBlank(user.getImageUrl())) {
            user.setImageUrl(CommonConstant.DEFAULT_IMAGE_AVATAR);
        }
        return new ComResponse<Boolean>().success(this.userService.save(user));
    }

    @GetMapping("/list/{page}/{limit}")
    public ComResponse<IPage<User>> listPage(@PathVariable("page") int page, @PathVariable("limit") int limit){
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.select("id","name","email","create_time","update_time");
        return new ComResponse<IPage<User>>().success(this.userService.page(new Page<User>(page,limit),wrapper),"查询成功");
    }
}
