package us.abstracta.sparring.controller;

import org.springframework.web.bind.annotation.*;
import us.abstracta.sparring.issues.Issue;
import us.abstracta.sparring.issues.impl.BadSqlDAO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class IssueController {

    private List<Issue> issues;

    public IssueController(){
        issues= new ArrayList<>();
        issues.add(new BadSqlDAO());
    }

    /* get all issues */
    @CrossOrigin(origins = "*")
    @GetMapping("/issues")
    public List<Issue> getIssues() {
        return issues;

    }

    /* get all active issues */
    @CrossOrigin(origins = "*")
    @GetMapping("/issues/active")
    public List<Issue> getActiveIssues() {
        List<Issue> activeIssues= new ArrayList();
        for (Issue issue : issues) {
            if (issue.isOn()){
             activeIssues.add(issue);
            }
        }
        return activeIssues;
    }

    /* get all inactive issues */
    @CrossOrigin(origins = "*")
    @GetMapping("/issues/inactive")
    public List<Issue> getInactiveIssues() {
        List<Issue> inactiveIssues= new ArrayList();
        for (Issue issue : issues) {
            if (!issue.isOn()){
                inactiveIssues.add(issue);
            }
        }
        return inactiveIssues;
    }

    /* set an issue on */
    @CrossOrigin(origins = "*")
    @PutMapping("/issues/{id}/active")
    public boolean setIssueOn(@PathVariable("id") String id) {
        for (Issue issue : issues) {
            if (id.equals(issue.getName())){
                issue.setOn();
            }
        }
        return true;
    }

    /* set an issue off */
    @CrossOrigin(origins = "*")
    @PutMapping("/issues/{id}/inactive")
    public boolean setIssueOff(@PathVariable("id") String id) {
        for (Issue issue : issues) {
            if (id.equals(issue.getName())){
                issue.setOff();
            }
        }
        return true;
    }

}
