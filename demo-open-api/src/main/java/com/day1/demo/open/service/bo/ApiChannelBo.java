package com.day1.demo.open.service.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.day1.demo.mapper.enums.DsOpenApiUriEnum;
import com.day1.demo.mapper.model.DsOpenApiChannel;
import com.day1.demo.mapper.model.DsOpenApiChannelInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Optional;

/**
 * @author: LinHangHui
 * @Date: 2020/12/10 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiChannelBo {

    private DsOpenApiChannel apiChannel;

    private List<DsOpenApiChannelInterface> apiChannelInterfaceList;

    @Transient
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    public Optional<DsOpenApiChannelInterface> getChannelInterface(DsOpenApiUriEnum apiUriEnum) {
        return apiChannelInterfaceList.stream().
                filter(item -> item.getInterfaceUriIndex().equals(apiUriEnum)).
                findAny();
    }

}
