package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chenqian
 * @date 2024/12/26 19:28
 **/
@Getter
@AllArgsConstructor
public enum CompressSupport {
    GZIP((byte) 1),
    ;

    byte code;
}
