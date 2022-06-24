package com.jsw.jrb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsw.jrb.core.pojo.entity.Borrower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsw.jrb.core.pojo.vo.BorrowerApprovalVO;
import com.jsw.jrb.core.pojo.vo.BorrowerDetailVO;
import com.jsw.jrb.core.pojo.vo.BorrowerVO;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
public interface BorrowerService extends IService<Borrower> {

    void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId);

    Integer getStatusByUserId(Long userId);

    IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword);

    BorrowerDetailVO getBorrowerDetailVOById(Long id);

    void approval(BorrowerApprovalVO borrowerApprovalVO);
}
