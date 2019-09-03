package com.learn.majiang.cache;

import com.learn.majiang.dto.TagDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagCache {
    public static List<TagDto> getTags(){
        ArrayList<TagDto> tagDtos = new ArrayList<>();
        TagDto program = new TagDto();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("Java","Js","Php"));
        tagDtos.add(program);

        TagDto frame = new TagDto();
        frame.setCategoryName("平台对象");
        frame.setTags(Arrays.asList("Spring","Struct2"));
        tagDtos.add(frame);
        return tagDtos;
    }
}
