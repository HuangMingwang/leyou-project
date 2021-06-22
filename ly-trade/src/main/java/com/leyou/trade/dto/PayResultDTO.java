package com.leyou.trade.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author Huang Mingwang
 * @create 2021-06-12 6:22 上午
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class PayResultDTO {
    @JacksonXmlProperty(localName = "return_code")
    private String returnCode = "SUCCESS";
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg = "OK";
}
