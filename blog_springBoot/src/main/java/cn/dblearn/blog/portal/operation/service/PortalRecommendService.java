package cn.dblearn.blog.portal.operation.service;

import cn.dblearn.blog.core.entity.operation.Recommend;
import cn.dblearn.blog.core.entity.operation.vo.RecommendVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ManageRecommendService
 *
 * @author bobbi
 * @date 2019/02/22 13:41
 * @email 571002217@qq.com
 * @description
 */
public interface PortalRecommendService extends IService<Recommend> {

    List<RecommendVO> listRecommendVo();

    List<RecommendVO> listHotRead();
}