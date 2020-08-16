<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use Illuminate\Support\Facades\Auth;//INI UNTUK KEPENTINGAN LOGIN

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
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function loginCustomer(request $request){
        $user = User::where('user_id',$request->user_id)->whereUserRole('1')->get();
        if(count($user)>0){
            if(Auth::attempt([
                'user_id'=>$request->user_id,
                'password'=>$request->password
            ])){
                return json_encode([
                    'is_error' => '0',
                    'message' => 'login berhasil'
                ]);
            }
            else{
                return json_encode([
                    'is_error' => '1',
                    'message' => 'user atau kata sandi salah'
                ]);
            }
        }
        else{
            return json_encode([
                'is_error' => '1',
                'message' => 'user atau kata sandi salah'
            ]);
        }        
    }

    public function loginAdmin(request $request){
        $user = User::where('user_id',$request->user_id)->whereUserRole('0')->get();
        if(count($user)>0){
            if(Auth::attempt([
                'user_id'=>$request->user_id,
                'password'=>$request->password
            ])){
                return json_encode([
                    'is_error' => '0',
                    'message' => 'login berhasil'
                ]);
            }
            else{
                return json_encode([
                    'is_error' => '1',
                    'message' => 'user atau kata sandi salah'
                ]);
            }
        }
        else{
            return json_encode([
                'is_error' => '1',
                'message' => 'user atau kata sandi salah'
            ]);
        }        
    }

    public function getAllCustomerUser(){
        return json_encode([
            'result' => User::userRole(1)->get() 
        ]);
    }
    
    public function getUserIdAndPasswordCustomer(request $request){
        $user = User::userId($request->user_id)->userRole(1)->get();
        if(count($user)>0){
            return json_encode([
                'is_error' => '0',
                'message' => 'berhasil',
                'user' => $user
            ]); 
        }

        return json_encode([
            'is_error' => '1',
            'message' => 'user tidak tersedia'
        ]);
    }

    public function testApi(request $request){
        return $request->test == null ? "test" : $request->test;
    }
}
