# XURL: URL Shortening Utility

**XURL** is a Java-based utility designed for creating and managing short URLs. This tool simplifies the use of long URLs by converting them into shorter, more manageable links. Additionally, it tracks URL lookups and supports custom short URL registrations.

## Features
![image](https://github.com/user-attachments/assets/ab15c81f-e471-4577-b37d-e67daeaa8301)


- **Automatic Short URL Generation**: Converts long URLs into random 9-character short URLs.
- **Custom Short URL Registration**: Allows users to specify a custom short URL.
- **URL Lookup**: Retrieves the original long URL using a short URL.
- **URL Deletion**: Removes URL mappings when they are no longer needed.
- **Hit Counting**: Tracks how many times a long URL has been accessed through its short URL.

## Scope of Work

1. **Class Implementation**:
   - Created a concrete class implementing the short URL interface with methods for URL mapping.

2. **Short URL Generation**:
   - **Method**: `generateShortURL(String longURL)`
   - **Description**: Generates a random 9-character short URL for a given long URL and stores the mapping in a hash map.

3. **Custom Short URL Registration**:
   - **Method**: `registerCustomURL(String longURL, String customShortURL)`
   - **Description**: Registers a custom short URL for a long URL, ensuring unique mappings.

4. **URL Lookup**:
   - **Method**: `lookupLongURL(String shortURL)`
   - **Description**: Retrieves the long URL from a given short URL.

5. **URL Deletion**:
   - **Method**: `deleteURL(String longURL)`
   - **Description**: Deletes the mapping for the specified long URL.

6. **Hit Count Tracking**:
   - **Method**: `getHitCount(String longURL)`
   - **Description**: Provides the number of times a long URL has been accessed through its short URL.

7. **Error Handling**:
   - Ensures each long URL maps to only one short URL and vice versa, handling any conflicts appropriately.

8. **Testing**:
   - Comprehensive test cases are included to verify the functionality of all methods.

## Example Usage

Below is a simple example demonstrating how to use the XURL utility:

```java
public class Main {
    public static void main(String[] args) {
        XURL xurl = new XURLImpl();

        // Register a long URL and get a short URL
        String longURL = "https://www.example.com/very/long/url";
        String shortURL = xurl.generateShortURL(longURL);
        System.out.println("Short URL: " + shortURL);

        // Register a custom short URL
        String customShortURL = "short.ly/abc123";
        xurl.registerCustomURL(longURL, customShortURL);

        // Lookup the long URL from the short URL
        String retrievedLongURL = xurl.lookupLongURL(customShortURL);
        System.out.println("Long URL: " + retrievedLongURL);

        // Get hit count
        int hitCount = xurl.getHitCount(longURL);
        System.out.println("Hit Count: " + hitCount);

        // Delete URL mapping
        xurl.deleteURL(longURL);
    }
}
