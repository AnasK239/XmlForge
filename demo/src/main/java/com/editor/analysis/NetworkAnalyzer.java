package com.editor.analysis;

import java.util.List;
import java.util.ArrayList;
import com.editor.structures.graph.SocialNetwork;
import com.editor.structures.graph.UserNode;
import com.editor.structures.graph.UserDirectory;

public class NetworkAnalyzer {
    // Role: Implement network queries.
    // xml_editor format -i src/main/resources/samples/sample.xml -o output_file.xml

    public UserNode mostInfluencer(SocialNetwork network) {
        // user with the largest number of followers.
        UserDirectory userDirectory = network.getUserDirectory();
        List<UserNode> users = userDirectory.getAllUsers();
        UserNode mostInfluencerUser = null;
        int maxInDegree = -1;

        for (UserNode currentUser : users) {
            int currentInDegree = currentUser.getFollowerIds().size();
            if (currentInDegree > maxInDegree) {
                maxInDegree = currentInDegree;
                mostInfluencerUser = currentUser;
            }
        }
        return mostInfluencerUser;
    }
    public int mostInfluencerId(SocialNetwork network) {
        UserNode mostInfluencerUser = mostInfluencer(network);
        return mostInfluencerUser.getId();
    }

    public UserNode mostActive(SocialNetwork network) {

        // user with largest out-degree degree 
        UserDirectory userDirectory = network.getUserDirectory();
        List<UserNode> users = userDirectory.getAllUsers();
        UserNode mostActiveUser = null;
        int maxOutDegree = -1;

        for (UserNode currentUser : users) {
            int currentOutDegree = currentUser.getFollowingIds().size();
            if (currentOutDegree > maxOutDegree) {
                maxOutDegree = currentOutDegree;
                mostActiveUser = currentUser;
            }
        }
        return mostActiveUser;
    }
    public int mostActiveId(SocialNetwork network) {
        UserNode mostActiveUser = mostActive(network);
        return mostActiveUser.getId();
    }

    public List<Integer> mutualFollowersIds(SocialNetwork network, int[] ids) {

        List<Integer> mutualFollowers = new ArrayList<>();
        if (ids == null || ids.length == 0) {
            return mutualFollowers;
        }

        UserDirectory userDirectory = network.getUserDirectory();
        UserNode firstUser = userDirectory.getById(ids[0]);
        if(firstUser == null)
            return mutualFollowers;
        List<Integer> commonFollowerIds = firstUser.getFollowerIds(); 


        for (int i = 1; i < ids.length; i++) { 
            UserNode currentUser = userDirectory.getById(ids[i]);
            if (currentUser == null) {
                return new ArrayList<>(); // If any user is not found, no mutual followers
            }
            List<Integer> currentFollowerIds = currentUser.getFollowerIds();
            mutual(commonFollowerIds, currentFollowerIds);
        }

        
        
        return commonFollowerIds;
        
    }
    public void mutual(List<Integer> list1, List<Integer> list2) {
        List<Integer> toRemove = new ArrayList<>();
        for (Integer element : list1) {
            if (!list2.contains(element)) {
                toRemove.add(element);
            }
        }
        list1.removeAll(toRemove);
    }

    public List<UserNode> mutualFollowers(SocialNetwork network, int[] ids) {
        List<Integer> mutualFollowersIdIntegers = mutualFollowersIds(network, ids);
        List<UserNode> mutualFollowers = new ArrayList<>();
        UserDirectory userDirectory = network.getUserDirectory();
        for (Integer followerId : mutualFollowersIdIntegers) {
            UserNode follower = userDirectory.getById(followerId);
            if (follower != null) {
                mutualFollowers.add(follower);
            }
        }
        return mutualFollowers;
    }

    public List<UserNode> suggestUsersToFollow(SocialNetwork network, int id) {
        List<UserNode> suggestedUsers = new ArrayList<>();
        UserNode currentUser = network.getUserDirectory().getById(id);
        if (currentUser == null) {
            return suggestedUsers;
        }

        setLike<Integer> followersOfFollowersIds = new setLike<>();
        List<Integer> myFollowingIds = currentUser.getFollowingIds();


        followersOfFollowersIds.add(id); 


        for (int followingId : myFollowingIds) {
            UserNode followingUser = network.getUserDirectory().getById(followingId);
            if (followingUser != null) {
                for (int fofId : followingUser.getFollowerIds()) {
                    if (fofId != id && !myFollowingIds.contains(fofId)) {
                        followersOfFollowersIds.add(fofId);
                    }
                }
            }
        }

        if (followersOfFollowersIds.getList() != null) {
            for (Integer suggestedId : followersOfFollowersIds.getList()) {
                UserNode suggestedUser = network.getUserDirectory().getById(suggestedId);
                if (suggestedUser != null) {
                    suggestedUsers.add(suggestedUser);
                }
            }
        }
        return suggestedUsers;
    }
    public List<Integer> suggestUsersToFollowIds(SocialNetwork network, int id) {
        List<UserNode> suggestedUsers = suggestUsersToFollow(network, id);
        List<Integer> suggestedUsersIds = new ArrayList<>();
        for (UserNode user : suggestedUsers) {
            suggestedUsersIds.add(user.getId());
        }
        return suggestedUsersIds;
    }


}
class setLike<E> {
    private ArrayList<E> list;

    void add(E element) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (!list.contains(element)) {
            list.add(element);
        }
    }
    void remove(E element) {
        list.add(element);
    }
    boolean contains(E element) {
        return list.contains(element);
    }
    ArrayList<E> getList()
    {
        return this.list;
    }
    
}
