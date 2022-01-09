package zatribune.spring.jasperreports.config;


import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class ResourcesLoader implements CommandLineRunner {


    private List<JasperReport>reports=new ArrayList<>(1);
    private List<BufferedImage>images=new ArrayList<>(1);



    @Override
    public void run(String... args) throws Exception {

        InputStream invoiceEn
                = getClass().getResourceAsStream("/static/templates/invoice_en.jrxml");
        JasperReport jasperReport1
                = JasperCompileManager.compileReport(invoiceEn);
        reports.add(jasperReport1);

        InputStream invoiceAr
                = getClass().getResourceAsStream("/static/templates/invoice_ar.jrxml");
        JasperReport jasperReport2
                = JasperCompileManager.compileReport(invoiceAr);
        reports.add(jasperReport2);


        BufferedImage image=ImageIO.read(getClass().getResourceAsStream("/static/images/bank.png"));
        images.add(image);
    }


    public void loadTemplates(){

    }

    public void loadImages(){

    }

}
