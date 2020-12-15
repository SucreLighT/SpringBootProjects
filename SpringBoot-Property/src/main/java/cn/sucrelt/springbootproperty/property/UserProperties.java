package cn.sucrelt.springbootproperty.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/12/15
 * @time: 15:20
 */

@Component
@ConfigurationProperties(prefix = "students")
@PropertySource(value = "classpath:students.properties", encoding = "UTF-8")
@Setter
@Getter
@ToString
public class UserProperties {
    private String type;
    private List<Stu> stu;

    @Setter
    @Getter
    @ToString
    static class Stu {
        private String username;
        private int age;
        private String city;
        private double money;
    }
}
