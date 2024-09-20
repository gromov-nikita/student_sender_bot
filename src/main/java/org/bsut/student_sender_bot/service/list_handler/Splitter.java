package org.bsut.student_sender_bot.service.list_handler;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class Splitter {
    public  <T> List<List<T>> split(List<T> list, Integer chunkSize) {
        return IntStream.range(0, (list.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> list.subList(i * chunkSize, Math.min(i * chunkSize + chunkSize, list.size())))
                .collect(Collectors.toList());
    }
}
