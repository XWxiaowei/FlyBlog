package com.fly.exception;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xiang.wei on 2018/10/27
 *
 * @author xiang.wei
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error("------------------>捕获到全局异常", e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", req.getRequestURI());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public R jsonErrorHandler(HttpServletRequest req, MyException e) {

        return R.failed(e.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ModelAndView assertExceptionHandler(HttpServletRequest req,
                                               IllegalArgumentException e) {
        log.error("----------------->捕捉assert异常", e);
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg", e.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/500");

        return mav;
    }
}
