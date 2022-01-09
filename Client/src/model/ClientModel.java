package model;

import java.io.Serializable;
import java.util.LinkedList;

public class ClientModel implements Serializable {
    private LinkedList<Company> companies;
    private User user;

    public ClientModel(LinkedList<Company> companies, User user) {
        this.companies = companies;
        this.user = user;
    }

    public void setCompanies(LinkedList<Company> companies) {
        this.companies = companies;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LinkedList<Company> getCompanies() {
        return companies;
    }

    public User getUser() {
        return user;
    }

    //https://stackabuse.com/search-algorithms-in-java/
    //Jump search implemented (https://www.geeksforgeeks.org/jump-search/)
    //Pre: companyName DEBE existir en la lista de compañias
    /**
     * Looks for a company that MUST exist in the LinkedList
     * @param name company name
     * @return Company found
     */
    public Company getCompanyByName(String name) {
        int size = companies.size();

        int step = (int) Math.floor(Math.sqrt(size));
        int prev = 0;
        while(companies.get(Math.min(step, size)-1).getName().compareTo(name) < 0) {
            prev = step;
            step += (int) Math.floor(Math.sqrt(size));
        }
        while (companies.get(prev).getName().compareTo(name) < 0) {
            prev++;
        }
        return companies.get(prev);
    }

    public int getIndexCompanyByName(String name) {
        int size = companies.size();

        int step = (int) Math.floor(Math.sqrt(size));
        int prev = 0;
        while(companies.get(Math.min(step, size)-1).getName().compareTo(name) < 0) {
            prev = step;
            step += (int) Math.floor(Math.sqrt(size));
        }
        while (companies.get(prev).getName().compareTo(name) < 0) {
            prev++;
        }
        return prev;
    }

    /**
     * Update share value of determinated company
     * @param name company to update share value
     * @param shareValue updated
     */
    public void updateShareValue(String name, float shareValue) {
        Company company = getCompanyByName(name);
        company.setShareValue(shareValue);
    }


}
