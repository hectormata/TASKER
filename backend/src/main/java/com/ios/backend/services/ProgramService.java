package main.java.com.ios.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.com.ios.backend.dto.NewProgramDTO;
import main.java.com.ios.backend.entities.Invites;
import main.java.com.ios.backend.entities.Program;
import main.java.com.ios.backend.entities.User;
import main.java.com.ios.backend.repositories.InvitesRepository;
import main.java.com.ios.backend.repositories.PasscodeRepository;
import main.java.com.ios.backend.repositories.ProgramRepository;
import main.java.com.ios.backend.repositories.UserRepository;
import main.java.com.ios.backend.resources.ProgramListResource;
import main.java.com.ios.backend.resources.ProgramResource;

@Service
public class ProgramService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ProgramRepository programRepository;
  @Autowired
  private PasscodeRepository passcodeRepository;
  @Autowired
  private InvitesRepository invitesRepository;
  @Autowired
  private MailService mailService;
  
  public boolean addUser(long uid, String code) {
    boolean invited = false;
    User user = userRepository.findById(uid).get();
    Long pidv = passcodeRepository.getPidByCode(code);
    if(! Objects.isNull(pidv)) {
      long pid = pidv.longValue();
      boolean ifInvited = invitesRepository.existsByUid(uid);
      List<Long> invitedPid = invitesRepository.findByUid(uid);
      boolean checked = false;
      for(Long id: invitedPid) {
        if(id == pid) {
          checked = true;
          break;
        }
      }
      
      if( ifInvited && checked) {
        System.out.println("###########");
        invited = true;
        Program p = programRepository.findById(pid).get();
        List<User> users = p.getUsers();
        users.add(user);
        p.setUsers(users);
        programRepository.save(p);
      } else {
        invited = false;
      }
    } else {
      invited = false;
    }
    return invited;
  }

  public long createProgram(NewProgramDTO newProgramDto, long admin) {
    Program program = new Program();
    program.setId(newProgramDto.getId());
    program.setName(newProgramDto.getName());
    program.setDescription(newProgramDto.getDescription());
    program.setAdmin(admin);
    User user = userRepository.findById(admin).get();
    List<User> userList = new ArrayList<User>();
    userList.add(user);
    program.setUsers(userList);
    Program savedProgram = programRepository.save(program);

    String code = mailService.generatePasscodeForProgram(savedProgram.getId());
    long[] users = newProgramDto.getUsers();
    inviteUsers(users, code, savedProgram.getId());
    
    return savedProgram.getId();
  }
  
  public void inviteUsers(long[] users, String code, long pid) {
    
    for( long uid: users) {
      String email = userRepository.findById(uid).get().getEmail();
      mailService.sendPasscode(email, code, programRepository.findById(pid).get());
      Invites invites = new Invites();
      invites.setPid(pid);
      invites.setUid(uid);
      invitesRepository.save(invites);
    }
  }
  
  public ProgramListResource getAll() {
    ProgramListResource list = new ProgramListResource();
    list.setProgramList( (List<ProgramResource>) programRepository.findAll().stream().map(p -> new ProgramResource(p)).collect(Collectors.toList()));
    return list;
  }
  
  public ProgramListResource getAllByAdmin(long id) {
    ProgramListResource list = new ProgramListResource();
    list.setProgramList( (List<ProgramResource>) programRepository.findByAdmin(id).stream().map(p -> new ProgramResource(p)).collect(Collectors.toList()));
    return list;
  }
}
