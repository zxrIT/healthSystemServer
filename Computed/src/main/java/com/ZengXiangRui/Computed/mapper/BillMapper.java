package com.ZengXiangRui.Computed.mapper;

import com.ZengXiangRui.Computed.entity.BillEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillMapper extends BaseMapper<BillEntity> {
    Double selectTotalAmountByUserId(@Param("userId") String userId);
    Integer calculateBillMonthsSpan(@Param("userId") String userId);
}
