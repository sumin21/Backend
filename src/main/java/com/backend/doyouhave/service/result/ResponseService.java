package com.backend.doyouhave.service.result;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ResponseService {
    public Result getSuccessResult() {
        Result result = new Result();
        setSuccessResult(result);
        return result;
    }

    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        setSuccessResult(result);
        result.setData(data);
        return result;
    }

    public <T> MultipleResult<T> getMultipleResult(List<T> data) {
        MultipleResult<T> result = new MultipleResult<>();
        setSuccessResult(result);
        result.setData(data);
        return result;
    }

    public <T> MultiplePageResult<T> getMultiplePageResult(Page<T> data) {
        MultiplePageResult<T> result = new MultiplePageResult<>();
        setSuccessResult(result);
        result.setPageData(data);
        return result;
    }

    public void setSuccessResult(Result result) {
        result.setSuccess(true);
        result.setCode(HttpStatus.OK);
        result.setMsg("성공");
    }
}
