package nz.ac.aucklanduni.service;

import nz.ac.aucklanduni.dao.ConditionDao;
import nz.ac.aucklanduni.model.Condition;
import nz.ac.aucklanduni.model.Tag;
import org.hibernate.HibernateException;
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
    public Condition createCondition(Condition condition) {
        try {
            conditionDao.save(condition);
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
        return condition;

    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public List<Condition> findAllConditions() {
        return this.conditionDao.findAllConditions();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public List<Condition> findAllConditions(int startIndex, int endIndex) {
        return this.conditionDao.findAllConditions(startIndex, endIndex);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public Long getAllConditionsCount() {
        return this.conditionDao.getAllConditionsCount();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public List<Condition> findCategoryConditions(String title, int startIndex, int endIndex) {
        return this.conditionDao.findCategoryConditions(title, startIndex, endIndex);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public Long getCategoryConditionsCount(String title) {
        return this.conditionDao.getCategoryConditionsCount(title);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public List<Condition> findSearchConditions(String term, int startIndex, int endIndex) {
        return this.conditionDao.findSearchConditions(term, startIndex, endIndex);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public Long getSearchConditionsCount(String term) {
        return this.conditionDao.getSearchConditionsCount(term);
    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly = true)
    public Condition find(Integer id) {
        return this.conditionDao.find(id);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public boolean delete(Integer id) {
        Condition condition = this.conditionDao.find(id);
        if (condition == null) {
            return false;
        }
        this.conditionDao.delete(condition);
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void delete(Condition condition) {
        this.conditionDao.delete(condition);
    }
}
