package cn.scut.xx.majorgraduation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 徐鑫
 */
@SpringBootApplication
@MapperScan("cn.scut.xx.majorgraduation.dao.mapper")
public class MajorGraduationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajorGraduationApplication.class, args);
    }

}
