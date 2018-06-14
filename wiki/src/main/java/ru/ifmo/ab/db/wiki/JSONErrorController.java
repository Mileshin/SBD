package ru.ifmo.ab.db.wiki;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class JSONErrorController implements ErrorController {
    private static final String path = "/error";
    private static final boolean stacktrace = false;

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = path)
    ErrorJSON error(HttpServletRequest request, HttpServletResponse response) {
        return new ErrorJSON(response.getStatus(),
                errorAttributes.getErrorAttributes(new ServletWebRequest(request), stacktrace));
    }

    @Override
    public String getErrorPath() {
        return path;
    }
}
