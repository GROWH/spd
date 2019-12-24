package org.jrplat.platform.util;

/**
 * Created by 赵腾飞 on 16-12-18.
 * ａｐｐ派单单据 的所有状态。
 * DJD 待结单
 * DQH  待取货
 * PSZ 配送中
 * YWC_YWC 已完成
 */
public class AppStateUtils {

    public static final String DJD_DJD = "待接单";     //接单　;　拒单
    public static final String DQH_HZCQH = "货主处取货";     //绑定运单号，称重，扫描物料，记录温度,生成费用
    public static final String DQH_ZZY = "自转运";     //记录温度
    public static final String DQH_THZZY = "退货自转运";     //绑定运单号 ,记录温度
    public static final String DQH_ZYQH = "转运取货";       //绑定运单号,称重,记录温度信息,生成费用
    public static final String DQH_FYTB = "费用同步";       //同步确费状态.
    public static final String PSZ_SDKH = "送达客户";       //到货签收 -记录温度,签收 ;退货签收 -记录温度,签收
    public static final String PSZ_DHC = "待回仓";         //记录温度,备货位信息
    public static final String YWC_YWC = "已完成";         //nothing

}
