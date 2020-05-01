package ml.socshared.adapter.vk.vkclient.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Paginator<PaginatingObject> {
    int count;
    int offset;
    List<PaginatingObject> response;
}
