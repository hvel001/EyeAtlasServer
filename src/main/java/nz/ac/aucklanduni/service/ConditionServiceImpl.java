package nz.ac.aucklanduni.service;

import nz.ac.aucklanduni.dao.ConditionDao;
import nz.ac.aucklanduni.model.Condition;
import nz.ac.aucklanduni.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("ConditionService")
public class ConditionServiceImpl implements ConditionService {

    @Autowired
    private ConditionDao conditionDao;

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public String createCondition(Condition condition) {
        if (this.conditionDao.find(condition.getTitle()) != null) {
            return "Failed to create condition, it already exists!";
        }
        conditionDao.save(condition);
        return "Condition was successfully created!";

    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public List<Condition> findAll() {
        return this.conditionDao.findAll();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public Condition find(String title) {
        return this.conditionDao.find(title);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public Long getConditionCount() {
        return this.conditionDao.getConditionCount();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public String delete(String title) {
        Condition condition = this.conditionDao.find(title);
        if (condition == null) {
            return "Condition " + title + " does not exist in database";
        }
        this.conditionDao.delete(condition);
        return "Condition was deleted successfully";
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void delete(Condition conditionDto) {
        this.conditionDao.delete(conditionDto);
    }
}
