package org.example.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoomType {

    CLASSROOM("Classroom"),
    AUDITORY("Auditory");

    private final String presentation;

}
