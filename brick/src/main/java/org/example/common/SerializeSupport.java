package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chenqian
 * @date 2024/12/26 19:25
 **/
@Getter
@AllArgsConstructor
public enum SerializeSupport {
    PROTOSTUFF((byte) 1);

    byte code;
}
