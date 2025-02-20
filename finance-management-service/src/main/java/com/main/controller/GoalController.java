//package com.main.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.main.dto.GoalDto;
//import com.main.entity.Goal;
//import com.main.proxy.UserClient;  // Assuming you want user validation
//import com.main.service.GoalService;
//
//@RestController
//@RequestMapping("/api/goals")
//public class GoalController {
//
//    @Autowired
//    private GoalService goalService;
//
//    @Autowired
//    private UserClient userClient; // Assuming you need user validation
//
//    // Create a new goal
//    @PostMapping
//    public ResponseEntity<Goal> createGoal(@RequestBody GoalDto goalDto) {
//    	
//    	   Goal createdGoal = goalService.createGoalService(goalDto);
//           return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
////        if (goalDto == null || goalDto.getUserId() <= 0 || goalDto.getDescription() == null || goalDto.getDescription().isEmpty()) {
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
////        }
////
////        // Validate user existence by calling UserClient
////        try {
////            userClient.getUserById(goalDto.getUserId()); // This will throw an exception if user is not found
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////        }
////
////        // Proceed with goal creation
////        try {
////            Goal createdGoal = goalService.createGoalService(goalDto);
////            return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
//    }
//
//    // Get a goal by ID
//    @GetMapping("/{goalId}")
//    public ResponseEntity<Goal> getGoalById(@PathVariable int goalId) {
//    	
//          Goal goal = goalService.getGoalByIdService(goalId);
//    	  return new ResponseEntity<>(goal, HttpStatus.OK);
//    	  
////        if (goalId <= 0) {
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
////        }
////
////        try {
////            Goal goal = goalService.getGoalByIdService(goalId);
////            if (goal == null) {
////                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////            }
////            return new ResponseEntity<>(goal, HttpStatus.OK);
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
//    }
//
//    // Update a goal
//    @PutMapping("/{goalId}")
//    public ResponseEntity<Goal> updateGoal(@PathVariable int goalId, @RequestBody GoalDto goalDto) {
//    	
//    	
//    	Goal updatedGoal = goalService.updateGoalService(goalId, goalDto);
//        return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
//        
//        
////        if (goalId <= 0 || goalDto == null || goalDto.getDescription() == null || goalDto.getDescription().isEmpty()) {
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
////        }
////
////        // Validate user existence by calling UserClient
////        try {
////            userClient.getUserById(goalDto.getUserId()); // This will throw an exception if user is not found
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////        }
////
////        // Proceed with goal update
////        try {
////            Goal updatedGoal = goalService.updateGoalService(goalId, goalDto);
////            return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
////        } catch (RuntimeException e) {
////            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
//    }
//
//    // Delete a goal
//    @DeleteMapping("/{goalId}")
//    public ResponseEntity<Void> deleteGoal(@PathVariable int goalId) {
//    	
//    	goalService.deleteGoalService(goalId);
//    	 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
////        if (goalId <= 0) {
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
////        }
////
////        try {
////            boolean isDeleted = goalService.deleteGoalService(goalId);
////            if (!isDeleted) {
////                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////            }
////            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
//    }
//
//    // Get all goals for a user
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Goal>> getUserGoals(@PathVariable int userId) {
//    	
//    	
//    	
//    	  List<Goal> goals = goalService.getUserGoalsService(userId);
//    	 return new ResponseEntity<>(goals, HttpStatus.OK);
//    	 
////        if (userId <= 0) {
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
////        }
////
////        // Validate user existence by calling UserClient
////        try {
////            userClient.getUserById(userId); // This will throw an exception if user is not found
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////        }
////
////        // Proceed with fetching user goals
////        try {
////            List<Goal> goals = goalService.getUserGoalsService(userId);
////            if (goals.isEmpty()) {
////                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////            }
////            return new ResponseEntity<>(goals, HttpStatus.OK);
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
//    }
//
//    // Track progress of a specific goal
//    @GetMapping("/{goalId}/progress")
//    public ResponseEntity<Double> trackGoalProgress(@PathVariable int goalId) {
//    	
//    	
//    	  goalService.trackGoalProgressService(goalId);
//    	  return new ResponseEntity<>(HttpStatus.OK);
////    	  
////        if (goalId <= 0) {
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
////        }
////
////        try {
////            double progress = goalService.trackGoalProgressService(goalId);
////            return new ResponseEntity<>(progress, HttpStatus.OK);
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////package com.main.controller;
////
////import java.util.List;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.DeleteMapping;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.PathVariable;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.PutMapping;
////import org.springframework.web.bind.annotation.RequestBody;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RestController;
////
////import com.main.dto.GoalDto;
////import com.main.entity.Goal;
////import com.main.service.GoalService;
////
////@RestController
////@RequestMapping("/api/goals")
////public class GoalController {
////
////	 @Autowired
////	    private GoalService goalService;
////
////	    // Create a new goal
////	    @PostMapping
////	    public ResponseEntity<Goal> createGoal(@RequestBody GoalDto goalDto) {
////	        Goal createdGoal = goalService.createGoalService(goalDto);
////	        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
////	    }
////
////	    // Get a goal by ID
////	    @GetMapping("/{goalId}")
////	    public ResponseEntity<Goal> getGoalById(@PathVariable int goalId) {
////	        Goal goal = goalService.getGoalByIdService(goalId);
////	        return new ResponseEntity<>(goal, HttpStatus.OK);
////	    }
////
////	    // Update a goal
////	    @PutMapping("/{goalId}")
////	    public ResponseEntity<Goal> updateGoal(@PathVariable int goalId, @RequestBody GoalDto goalDto) {
////	        Goal updatedGoal = goalService.updateGoalService(goalId, goalDto);
////	        return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
////	    }
////
////	    // Delete a goal
////	    @DeleteMapping("/{goalId}")
////	    public ResponseEntity<Void> deleteGoal(@PathVariable int goalId) {
////	        goalService.deleteGoalService(goalId);
////	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
////	    }
////
////	    // Get all goals for a user
////	    @GetMapping("/user/{userId}")
////	    public ResponseEntity<List<Goal>> getUserGoals(@PathVariable int userId) {
////	        List<Goal> goals = goalService.getUserGoalsService(userId);
////	        return new ResponseEntity<>(goals, HttpStatus.OK);
////	    }
////
////	    // Track progress of a specific goal
////	    @GetMapping("/{goalId}/progress")
////	    public ResponseEntity<Double> trackGoalProgress(@PathVariable int goalId) {
////	        double progress = goalService.trackGoalProgressService(goalId);
////	        return new ResponseEntity<>(progress, HttpStatus.OK);
////           }
////
////}