//package com.main.service;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.main.dto.GoalDto;
//import com.main.entity.Goal;
//import com.main.exception.GoalNotFoundException; // Custom exception for goal not found
//import com.main.exception.InvalidGoalException; // Custom exception for invalid goal data
//import com.main.repository.GoalRepository;
//
//@Service
//public class GoalServiceImpl implements GoalService {
//
//    private static final Logger logger = LoggerFactory.getLogger(GoalServiceImpl.class);
//
//    @Autowired
//    private GoalRepository goalRepo;
//
//    @Override
//    public Goal createGoalService(GoalDto goalDto) {
//        logger.info("Creating new goal for user ID: {}", goalDto.getUserId());
//
//        if (goalDto.getTargetAmount() <= 0) {
//            logger.error("Invalid target amount: {}", goalDto.getTargetAmount());
//            throw new InvalidGoalException("Target amount must be positive.");
//        }
//
//        Goal goal = new Goal();
//        goal.setUserId(goalDto.getUserId());
//        goal.setTargetAmount(goalDto.getTargetAmount());
//        goal.setCurrentAmount(goalDto.getCurrentAmount());
//        goal.setGoalName(goalDto.getDescription());
//        goal.setTargetDate(goalDto.getTargetDate());
//
//        logger.info("Goal created successfully for user ID: {}", goalDto.getUserId());
//        return goalRepo.save(goal);
//    }
//
//    @Override
//    public Goal getGoalByIdService(int goalId) {
//        logger.info("Fetching goal with ID: {}", goalId);
//        return goalRepo.findById(goalId)
//                .orElseThrow(() -> {
//                    logger.error("Goal not found with ID: {}", goalId);
//                    return new GoalNotFoundException("Goal not found with ID: " + goalId);
//                });
//    }
//
//    @Override
//    public Goal updateGoalService(int goalId, GoalDto goalDto) {
//        logger.info("Updating goal with ID: {}", goalId);
//
//        if (goalDto.getTargetAmount() <= 0) {
//            logger.error("Invalid target amount: {}", goalDto.getTargetAmount());
//            throw new InvalidGoalException("Target amount must be positive.");
//        }
//
//        Goal goal = getGoalByIdService(goalId); // Fetch the existing goal
//        goal.setTargetAmount(goalDto.getTargetAmount());
//        goal.setCurrentAmount(goalDto.getCurrentAmount());
//        goal.setGoalName(goalDto.getDescription());
//        goal.setTargetDate(goalDto.getTargetDate());
//
//        logger.info("Goal updated successfully for ID: {}", goalId);
//        return goalRepo.save(goal);
//    }
//
//    @Override
//    public void deleteGoalService(int goalId) {
//        logger.info("Deleting goal with ID: {}", goalId);
//        Goal goal = getGoalByIdService(goalId);
//        goalRepo.delete(goal);
//        logger.info("Goal deleted successfully for ID: {}", goalId);
//    }
//
//    @Override
//    public List<Goal> getUserGoalsService(int userId) {
//        logger.info("Fetching goals for user ID: {}", userId);
//        return goalRepo.findByUserId(userId);
//    }
//
//    @Override
//    public double trackGoalProgressService(int goalId) {
//        logger.info("Tracking progress for goal ID: {}", goalId);
//        Goal goal = getGoalByIdService(goalId);
//        
//        if (goal.getTargetAmount() <= 0) {
//            logger.error("Target amount must be positive to track progress: {}", goal.getTargetAmount());
//            throw new InvalidGoalException("Target amount must be positive.");
//        }
//
//        double progress = (goal.getCurrentAmount() / goal.getTargetAmount()) * 100;
//        logger.info("Progress tracked for goal ID: {} - Progress: {}", goalId, progress);
//        return progress;
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
////package com.main.service;
////
////import java.util.List;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////import com.main.dto.GoalDto;
////import com.main.entity.Goal;
////import com.main.repository.GoalRepository;
////
////@Service
////public class GoalServiceImpl implements GoalService{
////	
////
////	    @Autowired
////	    private GoalRepository goalRepo;
////
////	    @Override
////	    public Goal createGoalService(GoalDto goalDto) {
////	        Goal goal = new Goal();
////	        goal.setUserId(goalDto.getUserId());
////	        goal.setTargetAmount(goalDto.getTargetAmount());
////	        goal.setCurrentAmount(goalDto.getCurrentAmount());
////	        goal.setGoalName(goalDto.getDescription());
////	        goal.setTargetDate(goalDto.getTargetDate());
////	        return goalRepo.save(goal);
////	    }
////
////	    @Override
////	    public Goal getGoalByIdService(int goalId) {
////	        return goalRepo.findById(goalId)
////	                .orElseThrow(() -> new RuntimeException("Goal not found with ID: " + goalId));
////	    }
////
////	    @Override
////	    public Goal updateGoalService(int goalId, GoalDto goalDto) {
////	        Goal goal = getGoalByIdService(goalId);  // Fetch the existing goal
////	        goal.setTargetAmount(goalDto.getTargetAmount());
////	        goal.setCurrentAmount(goalDto.getCurrentAmount());
////	        goal.setGoalName(goalDto.getDescription());
////	        goal.setTargetDate(goalDto.getTargetDate());
////	        return goalRepo.save(goal);
////	    }
////
////	    @Override
////	    public void deleteGoalService(int goalId) {
////	        Goal goal = getGoalByIdService(goalId);
////	        goalRepo.delete(goal);
////	    }
////
////	    @Override
////	    public List<Goal> getUserGoalsService(int userId) {
////	        return goalRepo.findByUserId(userId);
////	    }
////
////	    @Override
////	    public double trackGoalProgressService(int goalId) {
////	        Goal goal = getGoalByIdService(goalId);
////	        return (goal.getCurrentAmount() / goal.getTargetAmount()) * 100;
////	    }
////
////}