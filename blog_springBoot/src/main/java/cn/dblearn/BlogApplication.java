package cn.dblearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		/**
		 * ElasticSearch 所需的临时设置，待解决
		 */
		System.setProperty("es.set.netty.runtime.available.processors","false");
		SpringApplication.run(BlogApplication.class, args);
	}

}
