package org.example.expert.domain.user.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import jdk.dynalink.beans.StaticClass;

public record SliceResponse<T>(
	List<T> content,
	boolean hasNext
) {
	public static <T> SliceResponse<T> from(Slice<T> slice){
		return new SliceResponse<>(
			slice.getContent(),
			slice.hasNext()
		);
	}
}
