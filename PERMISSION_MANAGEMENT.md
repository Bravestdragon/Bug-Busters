# Permission Management System

## Overview

The application now includes a role-based permission management system with two user roles:

- **USER** - Regular user with limited permissions
- **ADMIN** - Administrator with full access

## Features

### Backend Implementation

1. **User Model** (`User.java`)
   - Added `role` field (default: "USER")
   - Added `isAdmin()` method for quick role checking

2. **Database Schema Updates**
   - Updated `users` table to include `role` column
   - Users table structure:
     ```sql
     CREATE TABLE users (
       id INT PRIMARY KEY AUTO_INCREMENT,
       username VARCHAR(50) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       email VARCHAR(100),
       role VARCHAR(20) DEFAULT 'USER'
     );
     ```

3. **Authentication Response** (`AuthResponse.java`)
   - Now includes user's role in login response
   - Frontend can display user's role immediately after login

4. **Controller Updates** (`UserController.java`)
   - Role information is returned in login response
   - User role can be viewed and managed through the API

### Frontend Implementation

1. **User List Component** (`UserList.vue`)
   - Displays user role with color coding:
     - ADMIN: Red badge
     - USER: Blue badge
   - Only admins can see and manage other users

2. **User Form Component** (`UserForm.vue`)
   - Role selection dropdown when creating/editing users
   - Options: "Regular User" and "Administrator"
   - Admins can assign and change user roles

3. **Authentication Service** (`auth.js`)
   - `isAdmin()` - Check if current user is admin
   - `getRole()` - Get current user's role
   - `getUsername()` - Get logged-in username
   - `isLoggedIn()` - Check if user has valid token
   - `logout()` - Clear all auth data

4. **Admin Panel Component** (`AdminPanel.vue`)
   - Shows current user's role
   - Only visible to admin users
   - Quick logout button

5. **App Component** (`App.vue`)
   - Displays current user's name and role in header
   - Dynamic navigation based on authentication status
   - Clean visual distinction for admin vs regular users

## Usage

### Register a New User
1. Click "Register"
2. Enter username, password, and email
3. User will be created with default "USER" role

### Login
1. Click "Login"
2. Enter credentials
3. After login, your role will be displayed in the header

### Manage User Roles (Admin Only)
1. Navigate to Users list
2. Click "Edit" on any user
3. Change the Role dropdown
4. Click "Update" to save changes

### Create Admin User
1. Create a new user via "Add User"
2. Select "Administrator" from Role dropdown
3. Click "Create"

## Database Migration

To update existing database to support roles, run:

```sql
ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'USER';
```

Or update the first user to admin:

```sql
UPDATE users SET role = 'ADMIN' WHERE id = 1;
```

## API Endpoints

### User Management
- `POST /api/users/login` - Login (returns token + role)
- `POST /api/users/register` - Register new user
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user (includes role)
- `PUT /api/users/{id}` - Update user (includes role)
- `DELETE /api/users/{id}` - Delete user

## Security Notes

- Roles are validated on the backend
- JWT tokens are used for authentication
- Password is hashed and never exposed in API responses
- Admin panel is frontend-only (implement backend authorization for production)
- For production: Add Spring Security annotations for backend authorization

## Future Enhancements

1. Fine-grained permissions (Create, Read, Update, Delete)
2. Role-based view restrictions
3. Audit logging for admin actions
4. Backend authorization checks using Spring Security
5. Permission decorators on controller methods
