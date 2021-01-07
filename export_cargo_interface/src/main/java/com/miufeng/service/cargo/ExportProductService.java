package com.miufeng.service.cargo;

import com.github.pagehelper.PageInfo;
import com.miufeng.domain.cargo.ExportProduct;
import com.miufeng.domain.cargo.ExportProductExample;

import java.util.List;

public interface ExportProductService {

	ExportProduct findById(String id);

	List<ExportProduct> findAll(ExportProductExample example);

	void save(ExportProduct exportProduct);

	void update(ExportProduct exportProduct);

	void delete(String id);

	PageInfo<ExportProduct> findByPage(ExportProductExample exportProductExample, int pageNum, int pageSize);
}
