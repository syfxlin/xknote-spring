import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        // 配置文件
        Properties props = new Properties();
        try {
            props.load(
                new FileInputStream(
                    System.getProperty("user.dir") +
                    "/src/main/resources/application-dev.properties"
                )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 根目录
        String projectPath = System.getProperty("user.dir");
        // 设置输出目录
        gc.setOutputDir(projectPath + "/src/main/java");
        // 设置注释中的作者
        gc.setAuthor("syfxlin");
        gc.setOpen(false);
        // 是否覆盖
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl((String) props.get("spring.datasource.url"));
        dsc.setDriverName(
            (String) props.get("spring.datasource.driver-class-name")
        );
        dsc.setUsername((String) props.get("spring.datasource.username"));
        dsc.setPassword((String) props.get("spring.datasource.password"));
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // 设置父级包名
        pc.setParent("me.ixk.xknote");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 下划线转驼峰，表名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 下划线转驼峰，列名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 开启 Lombok 模式
        strategy.setEntityLombokModel(true);
        strategy.setEntityBuilderModel(true);
        strategy.setEntitySerialVersionUID(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        // RestController 或者 Controller
        strategy.setRestControllerStyle(true);
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
