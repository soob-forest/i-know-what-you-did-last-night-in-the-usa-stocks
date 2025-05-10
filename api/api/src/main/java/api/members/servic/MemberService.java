package api.members.servic;

import api.members.domain.Member;
import api.members.domain.MemberRepository;
import api.members.dto.response.MemberDto;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  public Optional<MemberDto> findByUserId(String userId) {
    var member = memberRepository.findByUserId(userId);
    if (member.isPresent()) {
      return Optional.of(member.get().toMemberDto());
    }
    return Optional.empty();
  }

  @Transactional
  public MemberDto join(String userId) {
    var member = memberRepository.save(Member.builder().userId(userId).build());
    member.registerSubscription(Time.valueOf(LocalTime.of(9, 0, 0)));
    return member.toMemberDto();
  }
}
