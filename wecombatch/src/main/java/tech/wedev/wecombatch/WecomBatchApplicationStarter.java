package tech.wedev.wecombatch;

import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@Slf4j
@MapperScan("tech.wedev.wecombatch.dao")
@ImportResource(locations = "classpath:consumer.xml")
@SpringBootApplication(scanBasePackages = "tech.wedev")
public class WecomBatchApplicationStarter extends SpringBootServletInitializer {

    /**
     *用于支持通过外部容器方式启动
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        parserConfig.setAutoTypeSupport(true);
        parserConfig.setSafeMode(true);
        return builder.sources(WecomBatchApplicationStarter.class);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        parserConfig.setAutoTypeSupport(true);
        parserConfig.setSafeMode(true);
        SpringApplication.run(WecomBatchApplicationStarter.class, args);
        long endTime = System.currentTimeMillis();
        long cost = (endTime - startTime) / 1000;
        log.info(String.format("*******************%s has been started successfully and it takes %s seconds***************", "WECOM-BATCH", String.valueOf(cost)));
    }
}
