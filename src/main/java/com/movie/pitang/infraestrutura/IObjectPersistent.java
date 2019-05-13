package com.movie.pitang.infraestrutura;

import java.io.Serializable;

public interface IObjectPersistent<T> extends Serializable {
    T getId();
}
