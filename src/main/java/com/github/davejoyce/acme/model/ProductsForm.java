package com.github.davejoyce.acme.model;

import com.opencsv.CSVReader;
import lombok.Data;
import org.thymeleaf.util.DateUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductsForm {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @NotNull(message = "Product CSV entries cannot be null")
    @NotEmpty(message = "Missing required product CSV entries")
    private String csvProductEntries;

    public List<ProductForm> asList() throws Exception {
        List<ProductForm> productFormList = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new StringReader(csvProductEntries))) {
            String[] record;
            while (null != (record = csvReader.readNext())) {
                if (5 != record.length) {
                    throw new IllegalArgumentException("Incorrect product entry value count: " + record.length);
                }
                ProductForm pf = new ProductForm();
                pf.setCustomerName(record[0]);
                pf.setProductType(ProductType.fromLabel(record[1]));
                pf.setDomain(record[2]);
                pf.setStartDate(DATE_FORMAT.parse(record[3]));
                pf.setDuration(Integer.valueOf(record[4]));
                productFormList.add(pf);
            }
        }
        return productFormList;
    }

}
