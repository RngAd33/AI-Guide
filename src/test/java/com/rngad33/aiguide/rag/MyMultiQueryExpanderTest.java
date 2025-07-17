package com.rngad33.aiguide.rag;

import com.rngad33.aiguide.rag.custom.MyMultiQueryExpander;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyMultiQueryExpanderTest {

    @Resource
    private MyMultiQueryExpander myMultiQueryExpander;

    @Test
    void doExpand() {
        List<Query> queries = myMultiQueryExpander.expand("");
        Assertions.assertEquals(3, queries.size());
    }

}