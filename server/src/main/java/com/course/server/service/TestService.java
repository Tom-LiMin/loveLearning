package com.course.server.service;

import com.course.server.domain.Test;
import com.course.server.domain.TestExample;
import com.course.server.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {
    @Resource
    private TestMapper testMapper;

    public List<Test> getList() {
        TestExample testExample = new TestExample();
        // 相当于一个where条件
        testExample.createCriteria().andIdEqualTo("1");
        testExample.setOrderByClause("id asc");
        return testMapper.selectByExample(testExample);
    }

}
