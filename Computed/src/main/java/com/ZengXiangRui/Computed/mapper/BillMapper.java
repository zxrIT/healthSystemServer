package com.ZengXiangRui.Computed.mapper;

import com.ZengXiangRui.Computed.entity.BillEntity;
import com.ZengXiangRui.Computed.entity.DailyConsumptionDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillMapper extends BaseMapper<BillEntity> {
    Double selectTotalAmountByUserId(@Param("userId") String userId);
    Integer calculateBillMonthsSpan(@Param("userId") String userId);
    List<DailyConsumptionDTO> selectDailyConsumptionByDateRange(
            @Param("userId") String userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
