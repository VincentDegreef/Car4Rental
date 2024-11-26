package be.ucll.se.groep26backend.role.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.role.repo.RoleRepository;


@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;  
    
    public Role getRoleByName(String name) throws RoleServiceException {
        if(roleRepository.findByName(name) == null) {
            throw new RoleServiceException("Role not found");
        }        

        return roleRepository.findByName(name);
    
    }
    
}