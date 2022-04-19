package com.ridge.test;

import com.ridge.mapper.JSONMapper;
import com.ridge.test.domain.CurrentSystemSettings;

public class JSONMapperTest {
    public static void run() throws Exception {
        String testJsonString = "{\"id\": 1, \"systemId\": 22}";

        System.out.println(JSONMapper.convert(testJsonString, CurrentSystemSettings.class));
    }
}
