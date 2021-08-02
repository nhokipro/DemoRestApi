package com.DemoRestApi.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class RestResponseHandle {
    int resCode;
    String respDesc;
}
