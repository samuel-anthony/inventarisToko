<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;

class UserController extends Controller
{
    //password untuk user akan di pantek dengan udin123
    public function register(request $request){
        
        $user = new User;
        $user->name = $request->user_role == '1'? 'table' + count(User::userRole(1)->get()) : 'admin' + count(User::userRole(0)->get());
        $user->user_id = $request->user_id;
        $user->password = $request->user_role == '1'? 'udin123' : $request->password;
        $user->password = bcrypt($user->password);
        $user->user_role = $request->user_role == '1'? 1 : 0;
        $user->save();
        return "success";
    }

    public function login(request $request){
        
    }

    public function testApi(request $request){
        return $request->test == null ? "test" : $request->test;
    }
}
