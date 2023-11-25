package org.sopt.sopkathon.domain.letter.service;

import lombok.RequiredArgsConstructor;
import org.sopt.sopkathon.domain.letter.domain.Color;
import org.sopt.sopkathon.domain.letter.domain.Letter;
import org.sopt.sopkathon.domain.letter.dto.request.LetterCreateRequest;
import org.sopt.sopkathon.domain.letter.dto.request.LettersColorRequest;
import org.sopt.sopkathon.domain.letter.dto.response.LetterDetailResponse;
import org.sopt.sopkathon.domain.letter.dto.response.LettersColorResponse;
import org.sopt.sopkathon.domain.letter.repository.LetterRepository;
import org.sopt.sopkathon.domain.user.domain.User;
import org.sopt.sopkathon.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;
    @Transactional
    public void createLetter(LetterCreateRequest request){
        User user = userRepository.findByIdOrThrow(request.userId());
        Letter letter = Letter.createLetter(
                request.name(), request.toEmail(), request.color(), request.content(), user);
        letterRepository.save(letter);
    }

    public LettersColorResponse getLetterColors(LettersColorRequest request){
        User user = userRepository.findByIdOrThrow(request.userId());
        List<Color> colors = letterRepository.findAllColorsByUser(user);
        return LettersColorResponse.of(colors.size(), colors);
    }

    public LetterDetailResponse getLetter(Long letterId) {
        Letter letter = letterRepository.findByIdOrThrow(letterId);
        User user = userRepository.findByIdOrThrow(letter.getUser().getId());
        return LetterDetailResponse.of(
                letter.getName(), letter.getColor(), letter.getContent(), user.getName());
    }
}
