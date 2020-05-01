package ml.socshared.adapter.vk.domain.response;

import lombok.Data;

import java.util.List;

@Data
public class Page<PaginationObject> {

    private int page;
    private int size;
    boolean hasNext;
    boolean hasPrev;
    List<PaginationObject> object;
}
