package ma.projet.services;

import ma.projet.dao.IDao;
import ma.projet.entities.Role;
import ma.projet.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IDao<Role> {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role create(Role o) {
        return roleRepository.save(o);
    }

    @Override
    public boolean delete(Role o) {
        try {
            roleRepository.delete(o);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Role update(Role o) {
        return roleRepository.save(o);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
            return roleRepository.findById(id).orElse(null);
    }

}
