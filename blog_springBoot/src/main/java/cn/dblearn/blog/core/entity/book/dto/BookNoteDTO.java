package cn.dblearn.blog.core.entity.book.dto;

import cn.dblearn.blog.core.entity.book.BookNote;
import cn.dblearn.blog.core.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * BookNote
 *
 * @author bobbi
 * @date 2019/01/08 19:04
 * @email 571002217@qq.com
 * @description
 */
@Data
public class BookNoteDTO extends BookNote {

    private List<Tag> tagList;

}
