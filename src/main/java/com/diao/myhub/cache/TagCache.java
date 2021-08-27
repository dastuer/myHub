package com.diao.myhub.cache;

import com.diao.myhub.dto.TagDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TagCache {
    public List<TagDTO> getTagCache(){
        ArrayList<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO tagDTO = new TagDTO();
        int n=0;
        tagDTO.setCategoryName("开发语言");
        tagDTO.setTags(Arrays.asList("javascript","php","css","html","html5","java","node.js","python","c++","c","golang","objective-c","typescript","shell","swift","c#","sass","ruby","bash","less","asp.net","lua","scala","coffeescript","actionscript","rust","erlang","perl"));
        tagDTO.setCategoryId(++n);
        tagDTOS.add(tagDTO);

        tagDTO = new TagDTO();
        tagDTO.setCategoryName("平台框架");
        tagDTO.setTags(Arrays.asList("aravel","spring","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
        tagDTO.setCategoryId(++n);
        tagDTOS.add(tagDTO);

        tagDTO = new TagDTO();
        tagDTO.setCategoryName("服务器");
        tagDTO.setTags(Arrays.asList("linux","nginx","docker","apache","ubuntu","centos","缓存","tomcat","负载均衡","unix","hadoop","windows-server"));
        tagDTO.setCategoryId(++n);
        tagDTOS.add(tagDTO);

        tagDTO = new TagDTO();
        tagDTO.setCategoryName("数据库");
        tagDTO.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql","memcached","sqlserver","postgresql","sqlite"));
        tagDTO.setCategoryId(++n);
        tagDTOS.add(tagDTO);

        tagDTO = new TagDTO();
        tagDTO.setCategoryName("开发工具");
        tagDTO.setTags(Arrays.asList("git","github","visual-studio-code","vim","sublime-text","xcode","intellij-idea","eclipse","maven","ide","svn","visual-studio","atom","emacs","textmate","hg"));
        tagDTO.setCategoryId(++n);
        tagDTOS.add(tagDTO);

        return tagDTOS;

    }
    public Set<String> getAllTags(){
        List<TagDTO> tagCache = getTagCache();
        Set<String> allTags = new HashSet<>();
        for (TagDTO tagDTO : tagCache) {
            allTags.addAll(tagDTO.getTags());
        }
        return allTags;
    }
}
