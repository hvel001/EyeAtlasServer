package nz.ac.aucklanduni.controllers;

import nz.ac.aucklanduni.model.Condition;
import nz.ac.aucklanduni.model.ConditionUpload;
import nz.ac.aucklanduni.model.Tag;
import nz.ac.aucklanduni.service.CategoryService;
import nz.ac.aucklanduni.service.ConditionService;
import nz.ac.aucklanduni.service.TagService;
import nz.ac.aucklanduni.util.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ConditionController {

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @PostConstruct
    private void init() {

        // Rebuild search index in RAMDirectory
        conditionService.buildSearchIndex();
    }

    // Get Request for ALL
    @RequestMapping(value = "/rest/condition/all", method = RequestMethod.GET)
    public @ResponseBody
    List<Condition> getAllConditions() {
        return conditionService.findAllConditions();
    }

    @RequestMapping(value = "/rest/condition/all/{startIndex}/{endIndex}", method = RequestMethod.GET)
    public @ResponseBody
    List<Condition> getAllConditions(@PathVariable("startIndex") int startIndex,@PathVariable("endIndex") int endIndex) {
        return conditionService.findAllConditions(startIndex, endIndex);
    }

    @RequestMapping(value = "/rest/condition/all/count", method = RequestMethod.GET)
    public @ResponseBody
    Long getAllConditionCount() {
        return conditionService.getAllConditionsCount();
    }

    // Get Request for Category
    @RequestMapping(value = "/rest/condition/category/{categoryId}", method = RequestMethod.GET)
    public @ResponseBody
    List<Condition> getCategoryConditions(@PathVariable("categoryId") String categoryId) {
        return conditionService.findCategoryConditions(categoryId);
    }

    @RequestMapping(value = "/rest/condition/category/{categoryId}/{startIndex}/{endIndex}", method = RequestMethod.GET)
    public @ResponseBody
    List<Condition> getCategoryConditions(@PathVariable("categoryId") String categoryId, @PathVariable("startIndex") int startIndex,@PathVariable("endIndex") int endIndex) {
        return conditionService.findCategoryConditions(categoryId, startIndex, endIndex);
    }

    @RequestMapping(value = "/rest/condition/category/{categoryId}/count", method = RequestMethod.GET)
    public @ResponseBody
    Long getCategoryConditionCount(@PathVariable("categoryId") String categoryId) {
        return conditionService.getCategoryConditionsCount(categoryId);
    }

    // Get Request for Search term
    @RequestMapping(value = "/rest/condition/search/{term}", method = RequestMethod.GET)
    public @ResponseBody
    List<Condition> getSearchConditions(@PathVariable("term") String term) {
        return conditionService.findSearchConditions(term);
    }

    @RequestMapping(value = "/rest/condition/search/{term}/{startIndex}/{endIndex}", method = RequestMethod.GET)
    public @ResponseBody
    List<Condition> getSearchConditions(@PathVariable("term") String term, @PathVariable("startIndex") int startIndex,@PathVariable("endIndex") int endIndex) {
        return conditionService.findSearchConditions(term, startIndex, endIndex);
    }

    // Post Request for conditions
    @RequestMapping(value = "/rest/condition", method = RequestMethod.POST)
    public String conditionUpload(@RequestBody ConditionUpload conditionUpload) {

        // Value checks
        if (conditionUpload.getCondition().getTitle() == null || conditionUpload.getCondition().getTitle().equals("")) {
            return "The Condition must have a title!";
        } else if (conditionUpload.getCategory() == null || conditionUpload.getCategory().equals("")) {
            return "The Condition must have a category!";
        } else if (conditionUpload.getCondition().getImageWidth() == null || conditionUpload.getCondition().getImageHeight() == null) {
            return "The Condition must have an image width or image height!";
        }

        Condition condition = conditionUpload.getCondition();
        condition.setCategory(categoryService.find(conditionUpload.getCategory()));

        Set<Tag> tagSet = new HashSet<Tag>();
        for(String name : conditionUpload.getTags()) {
            tagSet.add(tagService.find(name));
        }

        condition.setTags(tagSet);

        Condition result = conditionService.createCondition(condition);

        if (result != null) {
            try {
                ImageProcessor.process(condition.getId().toString(), conditionUpload.getImage());
            } catch (Exception e) {
                e.printStackTrace();
                conditionService.delete(result);
                return "Condition creation was UNSUCCESSFUL!";
            }
        }

        return "Condition creation was SUCCESSFUL!";
    }

    // Delete request for conditions
    @RequestMapping(value = "/rest/condition/{id}", method = RequestMethod.DELETE)
    public String conditionDelete(@PathVariable("id") Integer id) {

        Condition condition = this.conditionService.find(id);

        if(condition == null) {
            return "Condition with ID: " + id + " does not exist.";
        }

        boolean deleted =  this.conditionService.delete(condition.getId());

        if(!deleted) {
            return "Condition deletion UNSUCCESSFUL!";
        }

        try {
            ImageProcessor.deleteImage(condition.getId().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return  "Condition data deletion SUCCESSFUL.\n" +
                    "Image deletion UNSUCCESSFUL.\n" +
                    "Please delete the image from the file storage manually.\n" +
                    "IMAGE ID: " + condition.getId();
        }

        return "Condition deletion SUCCESSFUL!";
    }

}
